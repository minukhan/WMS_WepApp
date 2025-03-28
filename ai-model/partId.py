import pandas as pd
import tensorflow as tf
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import os
import requests
import json
from datetime import datetime, timedelta
import certifi
from sklearn.model_selection import train_test_split
from dotenv import load_dotenv
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score



# .env 파일 로드
load_dotenv()

# 환경 변수 가져오기
SERVICE_KEY = os.getenv("SERVICE_KEY")

if not SERVICE_KEY:
    raise ValueError("🚨 SERVICE_KEY가 설정되지 않았습니다. .env 파일을 확인하세요.")

# Load data
sales_data = pd.read_csv('mock_sales_data.csv')
exchange_data = pd.read_csv('exchange.csv')
hyundai_car_stock_data = pd.read_csv('hyundai_stock_monthly_avg.csv')

# Data preprocessing
sales_data['date'] = pd.to_datetime(sales_data['date'], format='%Y-%m')
exchange_data['date'] = pd.to_datetime(exchange_data[['Year', 'Month']].assign(Day=1))
hyundai_car_stock_data['date'] = pd.to_datetime(hyundai_car_stock_data[['Year', 'Month']].assign(Day=1))

# Merge data
sales_data = sales_data.merge(exchange_data, how='left', on='date')
sales_data = sales_data.merge(hyundai_car_stock_data, how='left', on='date')
sales_data.ffill(inplace=True)

# Create features and labels
sales_data['month'] = sales_data['date'].dt.month
sales_data['year'] = sales_data['date'].dt.year

# 1. 부품별 데이터로 나누기
part_models = {}

for part_id in sales_data['part_id'].unique()[:5]:
    part_data = sales_data[sales_data['part_id'] == part_id]

    # Create features and labels
    X_part = part_data[['month', 'year', 'Exchange_rate', 'sales_quantity', 'Hyundai_Month_Stock_Average']]
    y_part = part_data['sales_quantity'].shift(-1).dropna()
    X_part = X_part[:-1]  # Remove the last row to align with y_part

    # Normalize data
    X_mean, X_std = X_part.mean(), X_part.std()
    X_part_normalized = (X_part - X_mean) / X_std
    y_mean, y_std = y_part.mean(), y_part.std()
    y_part_normalized = (y_part - y_mean) / y_std


    # TensorFlow model (부품별로 모델을 학습)
    model_part = tf.keras.Sequential([
        tf.keras.layers.Input(shape=(X_part_normalized.shape[1],)),
        tf.keras.layers.Dense(16, activation='relu'),
        tf.keras.layers.Dense(8, activation='relu'),
        tf.keras.layers.Dense(1)
    ])

    model_part.compile(optimizer='adam', loss='mean_squared_error', metrics=['mean_absolute_error'])

    # Train-test split
    X_train, X_val, y_train, y_val = train_test_split(X_part_normalized, y_part_normalized, test_size=0.2,
                                                      random_state=42)

    model_part.fit(X_train, y_train, epochs=30, batch_size=10, validation_data=(X_val, y_val), verbose=0)

    # 부품별 모델 저장
    part_models[part_id] = {
        'model': model_part,
        'X_mean': X_mean,
        'X_std': X_std,
        'y_mean': y_mean,
        'y_std': y_std
    }

    print("학습 완료 :", part_id)

# 🔹 예측 수행 및 성능 평가
for part_id in part_models:
    # 부품별 모델 가져오기
    part_model_info = part_models[part_id]
    model = part_model_info['model']
    X_mean = part_model_info['X_mean']
    X_std = part_model_info['X_std']
    y_mean = part_model_info['y_mean']
    y_std = part_model_info['y_std']

    # 부품별 데이터 준비
    part_data = sales_data[sales_data['part_id'] == part_id]
    X_part = part_data[['month', 'year', 'Exchange_rate', 'sales_quantity', 'Hyundai_Month_Stock_Average']]
    y_part = part_data['sales_quantity'].shift(-1).dropna()
    X_part = X_part[:-1]  # Remove the last row to align with y_part

    # Normalize data
    X_part_normalized = (X_part - X_mean) / X_std
    y_part_normalized = (y_part - y_mean) / y_std

    # 예측 수행
    predictions_normalized = model.predict(X_part_normalized)
    predictions = predictions_normalized * y_std + y_mean

    # 성능 평가
    mae = mean_absolute_error(y_part, predictions)
    mse = mean_squared_error(y_part, predictions)
    r2 = r2_score(y_part, predictions)

    # 성능 출력
    print(f"📊 {part_id} 부품의 예측 성능:")
    print(f"📊 Mean Absolute Error (MAE): {mae:.2f}")
    print(f"📊 Mean Squared Error (MSE): {mse:.2f}")
    print(f"📊 R2 Score: {r2:.2f}\n")


# FastAPI app setup
app = FastAPI()


# API input model
class PredictRequest(BaseModel):
    part_id: str
    current_sales: int

def fetch_exchange_rate():
    response = requests.get("https://m.search.naver.com/p/csearch/content/qapirender.nhn?key=calculator&pkid=141&q=%ED%99%98%EC%9C%A8&where=m&u1=keb&u6=standardUnit&u7=0&u3=USD&u4=KRW&u8=down&u2=1")
    if response.status_code == 200:
        try:
            data = response.json()
            # Debug: Print the response to check the actual structure
            # print("API Response:", data)

            # Safely retrieve the exchange rate from the 'country' key
            if 'country' in data and len(data['country']) > 1:
                exchange_rate = float(data['country'][1]['value'].replace(",", ""))
                # print("실시간 환율 (USD)",exchange_rate)
                return exchange_rate
            else:
                raise HTTPException(status_code=500, detail="Unexpected response structure")
        except Exception as e:
            raise HTTPException(status_code=500, detail=f"Error parsing exchange rate: {str(e)}")
    else:
        raise HTTPException(status_code=500, detail="Failed to fetch exchange rate")

# Fetch Hyundai stock price
def fetch_hyundai_stock_price():
    # 저장된 마지막 업데이트 시간을 가져오기
    if os.path.exists("last_update.txt"):
        print("주식 값이 있습니다.")
        with open("last_update.txt", "r") as file:
            last_update = file.read().strip()
            last_update = datetime.strptime(last_update, '%Y-%m-%d')

        # 현재 날짜가 마지막 업데이트 날짜와 같은지 확인
        if datetime.now().date() == last_update.date():
            # 이미 오늘 주식 가격을 가져왔으면 저장된 값 사용
            with open("stock_price.json", "r") as file:
                stock_data = json.load(file)
                print("이미 있는 값 사용",stock_data["price"])
                return stock_data["price"]
    else:
        print("주식 값을 새로 만듭니다.")
        last_update = datetime.min  # 처음 실행 시 기본 날짜 설정

    # 주식 가격 가져오기
    for i in range(10):
        target_date = (datetime.now() - timedelta(days=i + 1)).strftime('%Y%m%d')
        params = {
            'serviceKey': SERVICE_KEY,
            'resultType': 'json',
            'numOfRows': '1',
            'pageNo': '1',
            'basDt': target_date,
            'likeSrtnCd': '005380'
        }

        response = requests.get(
            "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo", params=params,
            verify=certifi.where())

        if response.status_code == 200:
            data = response.json()
            items = data['response']['body']['items']['item']
            if items:
                # 종가 저장
                stock_price = float(items[0]['clpr'])

                # 오늘의 주식 가격을 stock_price.json에 저장
                print("오늘의 주식 가격을 stock_price.json에 저장",stock_price)
                with open("stock_price.json", "w") as file:
                    json.dump({"price": stock_price}, file)

                # 마지막 업데이트 시간을 기록
                print("마지막 업데이트 시간을 기록",datetime.now().strftime('%Y-%m-%d'))
                with open("last_update.txt", "w") as file:
                    file.write(datetime.now().strftime('%Y-%m-%d'))

                return stock_price
        else:
            raise HTTPException(status_code=500, detail="Failed to fetch stock price")

    # 세 번의 날짜에 대해 모두 가격을 찾을 수 없으면 404 에러 발생
    raise HTTPException(status_code=404, detail="Stock price not found for the past 10 days")

@app.post("/predict")
def predict_sales(request: PredictRequest):
    part_id = request.part_id

    if part_id not in part_models:
        raise HTTPException(status_code=404, detail="Part ID not found")

    # 부품별 모델 가져오기
    part_model_info = part_models[part_id]
    model = part_model_info['model']
    X_mean = part_model_info['X_mean']
    X_std = part_model_info['X_std']
    y_mean = part_model_info['y_mean']
    y_std = part_model_info['y_std']

    # Prepare input data
    current_exchange_rate = fetch_exchange_rate()  # 환율 가져오기
    current_stock_price = fetch_hyundai_stock_price()  # 주식 가격 가져오기
    current_date = pd.Timestamp.now()

    input_data = pd.DataFrame({
        'month': [current_date.month],
        'year': [current_date.year],
        'Exchange_rate': [current_exchange_rate],
        'sales_quantity': [request.current_sales],
        'Hyundai_Month_Stock_Average': [current_stock_price]
    })
    input_data = (input_data - X_mean) / X_std  # 데이터 정규화

    # Predict sales using the specific part model
    predicted_sales = model.predict(input_data)[0][0]
    predicted_sales = predicted_sales * y_std + y_mean  # 예측 결과 복원

    return {
        'part_id': request.part_id,
        'current_month_sales': request.current_sales,
        'predicted_next_month_sales': max(10, int(predicted_sales)),
    }

