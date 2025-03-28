# import pandas as pd
# import numpy as np
# import tensorflow as tf
# import matplotlib.pyplot as plt
# from sklearn.model_selection import train_test_split
#
# # 🔹 데이터 로드
# sales_data = pd.read_csv('mock_sales_data.csv')
# exchange_data = pd.read_csv('exchange.csv')
# hyundai_stock_data = pd.read_csv('hyundai_stock_monthly_avg.csv')
#
# # 🔹 날짜 형식 변환
# sales_data['date'] = pd.to_datetime(sales_data['date'], format='%Y-%m')
# exchange_data['date'] = pd.to_datetime(exchange_data[['Year', 'Month']].assign(Day=1))
# hyundai_stock_data['date'] = pd.to_datetime(hyundai_stock_data[['Year', 'Month']].assign(Day=1))
#
# # 🔹 데이터 병합
# sales_data = sales_data.merge(exchange_data, how='left', on='date')
# sales_data = sales_data.merge(hyundai_stock_data, how='left', on='date')
# sales_data.fillna(method='ffill', inplace=True)
#
# # 🔹 특성 생성
# sales_data['month'] = sales_data['date'].dt.month
# sales_data['year'] = sales_data['date'].dt.year
#
# # 🔹 입력과 출력 데이터 설정
# X = sales_data[['month', 'year', 'Exchange_rate', 'Hyundai_Month_Stock_Average', 'sales_quantity']]
# y = sales_data['sales_quantity'].shift(-1).dropna()
# X = X[:-1]  # 마지막 샘플 제거
#
# # 🔹 데이터 분할
# X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42, shuffle=False)
#
# # 🔹 정규화
# X_mean, X_std = X_train.mean(), X_train.std()
# X_train_normalized = (X_train - X_mean) / X_std
# X_test_normalized = (X_test - X_mean) / X_std
#
# y_mean, y_std = y_train.mean(), y_train.std()
# y_train_normalized = (y_train - y_mean) / y_std
# y_test_normalized = (y_test - y_mean) / y_std
#
# # 🔹 모델 정의
# model = tf.keras.Sequential([
#     tf.keras.layers.Dense(16, activation='relu', input_shape=(X_train_normalized.shape[1],)),
#     tf.keras.layers.Dense(8, activation='relu'),
#     tf.keras.layers.Dense(1)
# ])
#
# model.summary()
#
# model.compile(optimizer='adam', loss='mean_squared_error', metrics=['mean_absolute_error'])
#
# # 🔹 모델 훈련
# history = model.fit(X_train_normalized, y_train_normalized, epochs=30, batch_size=10, verbose=1, validation_split=0.2)
#
# # 🔹 예측 수행
# predictions_normalized = model.predict(X_test_normalized)
# predictions = predictions_normalized * y_std + y_mean
#
# # 🔹 시각화
# plt.figure(figsize=(14, 7))
# plt.plot(y_test.values, label='Actual Sales', color='blue')
# plt.plot(predictions, label='Predicted Sales', color='orange')
# plt.title('Actual vs Predicted Sales with Hyundai Stock Influence')
# plt.xlabel('Time (Months)')
# plt.ylabel('Sales Quantity')
# plt.legend()
# plt.grid(True)
# plt.show()

# 아래는 LSTM 모델

import pandas as pd
import numpy as np
import tensorflow as tf
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split

# 🔹 데이터 로드
sales_data = pd.read_csv('mock_sales_data.csv')
exchange_data = pd.read_csv('exchange.csv')
hyundai_stock_data = pd.read_csv('hyundai_stock_monthly_avg.csv')

# 🔹 날짜 형식 변환
sales_data['date'] = pd.to_datetime(sales_data['date'], format='%Y-%m')
exchange_data['date'] = pd.to_datetime(exchange_data[['Year', 'Month']].assign(Day=1))
hyundai_stock_data['date'] = pd.to_datetime(hyundai_stock_data[['Year', 'Month']].assign(Day=1))

# 🔹 데이터 병합
sales_data = sales_data.merge(exchange_data, how='left', on='date')
sales_data = sales_data.merge(hyundai_stock_data, how='left', on='date')
sales_data.fillna(method='ffill', inplace=True)

# 🔹 특성 생성
sales_data['month'] = sales_data['date'].dt.month
sales_data['year'] = sales_data['date'].dt.year

# 🔹 입력과 출력 데이터 설정
X = sales_data[['month', 'year', 'Exchange_rate', 'Hyundai_Month_Stock_Average', 'sales_quantity']]
y = sales_data['sales_quantity'].shift(-1).dropna()
X = X[:-1]  # 마지막 샘플 제거

# 🔹 데이터 분할
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42, shuffle=False)

# 🔹 정규화
X_mean, X_std = X_train.mean(), X_train.std()
X_train_normalized = (X_train - X_mean) / X_std
X_test_normalized = (X_test - X_mean) / X_std

y_mean, y_std = y_train.mean(), y_train.std()
y_train_normalized = (y_train - y_mean) / y_std
y_test_normalized = (y_test - y_mean) / y_std

# 🔹 입력 데이터 형태 변경 (LSTM에 맞게)
X_train_reshaped = np.reshape(X_train_normalized.values, (X_train_normalized.shape[0], 1, X_train_normalized.shape[1]))
X_test_reshaped = np.reshape(X_test_normalized.values, (X_test_normalized.shape[0], 1, X_test_normalized.shape[1]))

# 🔹 LSTM 모델 정의
model = tf.keras.Sequential([
    tf.keras.layers.LSTM(32, activation='relu', input_shape=(X_train_reshaped.shape[1], X_train_reshaped.shape[2])),
    tf.keras.layers.Dense(16, activation='relu'),
    tf.keras.layers.Dense(1)
])

model.summary()

model.compile(optimizer='adam', loss='mean_squared_error', metrics=['mean_absolute_error'])

# 🔹 모델 훈련
history = model.fit(X_train_reshaped, y_train_normalized, epochs=40, batch_size=10, verbose=1, validation_split=0.2)

# 🔹 예측 수행
predictions_normalized = model.predict(X_test_reshaped)
predictions = predictions_normalized * y_std + y_mean

# 🔹 시각화
plt.figure(figsize=(14, 7))
plt.plot(y_test.values, label='Actual Sales', color='blue')
plt.plot(predictions, label='Predicted Sales', color='orange')
plt.title('Actual vs Predicted Sales with Hyundai Stock Influence (LSTM Model)')
plt.xlabel('Time (Months)')
plt.ylabel('Sales Quantity')
plt.legend()
plt.grid(True)
plt.show()

# 수정된 평가 지표 코드
mae = tf.keras.losses.MeanAbsoluteError()(y_test, predictions).numpy()
mse = tf.keras.losses.MeanSquaredError()(y_test, predictions).numpy()

print(f"📊 Mean Absolute Error (MAE): {mae:.2f}")
print(f"📊 Mean Squared Error (MSE): {mse:.2f}")

# AI 회귀 분석 모델 학습 결과 확인
import matplotlib.pyplot as plt
plt.figure(figsize=(12, 4))
plt.subplot(1, 2, 1)
plt.plot(history.history['loss'], 'b-', label='loss')
plt.plot(history.history['val_loss'], 'r--', label='val_loss')
plt.xlabel('Epoch')
plt.legend()
plt.subplot(1, 2, 2)
plt.plot(history.history['mean_absolute_error'], 'g-', label='mean_absolute_error')
plt.plot(history.history['val_mean_absolute_error'], 'k--', label='val_mean_absolute_error')
plt.xlabel('Epoch')
plt.legend()
plt.show()

# 수정된 평가 지표 코드
mae = tf.keras.losses.MeanAbsoluteError()(y_test, predictions).numpy()
mse = tf.keras.losses.MeanSquaredError()(y_test, predictions).numpy()

print(f"📊 Mean Absolute Error (MAE): {mae:.2f}")
print(f"📊 Mean Squared Error (MSE): {mse:.2f}")

# AI 회귀 분석 모델 학습 결과 확인
import matplotlib.pyplot as plt
plt.figure(figsize=(12, 4))
plt.subplot(1, 2, 1)
plt.plot(history.history['loss'], 'b-', label='loss')
plt.plot(history.history['val_loss'], 'r--', label='val_loss')
plt.xlabel('Epoch')
plt.legend()
plt.subplot(1, 2, 2)
plt.plot(history.history['mean_absolute_error'], 'g-', label='mean_absolute_error')
plt.plot(history.history['val_mean_absolute_error'], 'k--', label='val_mean_absolute_error')
plt.xlabel('Epoch')
plt.legend()
plt.show()

# 🔹 SavedModel 형식으로 저장
model.save("sales_prediction_model.keras")