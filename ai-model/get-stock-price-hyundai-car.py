import requests
import pandas as pd
from datetime import datetime
import xml.etree.ElementTree as ET
import certifi

# API 키와 기본 URL 설정
API_KEY = ""  # 본인의 API 키를 여기에 입력하세요
BASE_URL = "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo"
STOCK_CODE = "005380"

# 기간 설정
start_year, start_month = 2022, 11
end_year, end_month = 2024, 11

# 월별 데이터 저장
monthly_data = []

# 월별 데이터를 가져오는 함수
def fetch_monthly_data(year, month):
    start_date = datetime(year, month, 1).strftime("%Y%m%d")
    end_date = datetime(year, month + 1, 1).strftime("%Y%m%d") if month < 12 else datetime(year + 1, 1, 1).strftime("%Y%m%d")

    params = {
        "serviceKey": API_KEY,
        "numOfRows": 1000,
        "likeSrtnCd": STOCK_CODE,
        "resultType": "xml"
    }

    response = requests.get(BASE_URL, params=params,verify=certifi.where())
    if response.status_code != 200:
        print(f"❌ {year}-{month:02d} 데이터 요청 실패!")
        return None

    # XML 응답 파싱
    root = ET.fromstring(response.content)
    items = root.find(".//items")
    if items is None:
        print(f"⚠️ {year}-{month:02d} 데이터 없음.")
        return None

    # 종가 데이터 수집
    closing_prices = []
    for item in items.findall("item"):
        bas_dt = item.find("basDt").text
        clpr = item.find("clpr").text
        if clpr and bas_dt.startswith(f"{year}{month:02d}"):
            closing_prices.append(int(clpr))

    # 월별 평균 계산
    if closing_prices:
        avg_price = round(sum(closing_prices) / len(closing_prices), 2)
        print(f"✅ {year}-{month:02d} 평균 종가: {avg_price}")
        monthly_data.append([year, month, avg_price])
    else:
        print(f"⚠️ {year}-{month:02d}에 유효한 데이터가 없습니다.")

# 기간 동안 반복
current_year, current_month = start_year, start_month
while (current_year < end_year) or (current_year == end_year and current_month <= end_month):
    fetch_monthly_data(current_year, current_month)

    # 다음 달로 이동
    if current_month == 12:
        current_month = 1
        current_year += 1
    else:
        current_month += 1

# DataFrame 생성 및 CSV 저장
if monthly_data:
    df = pd.DataFrame(monthly_data, columns=["Year", "Month", "Hyundai_Month_Stock_Average"])
    df.to_csv("hyundai_stock_monthly_avg.csv", index=False, encoding="utf-8-sig")
    print("\n✅ CSV 파일이 성공적으로 저장되었습니다: hyundai_stock_monthly_avg.csv")
else:
    print("❌ 유효한 데이터가 없어 CSV 파일을 저장하지 않았습니다.")




# import requests
# import pandas as pd
# import xml.etree.ElementTree as ET
# from datetime import datetime, timedelta
# from collections import defaultdict
# import time
# import certifi
#
# # ✅ API 설정
# API_KEY = ""  # 👉 본인의 API 키 입력
# BASE_URL = "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo"
#
# # ✅ 현대차 종목 코드 (isinCd 또는 srtnCd 사용)
# STOCK_CODE = "005380"
#
# # ✅ 날짜 설정 (2022년 11월 ~ 2024년 11월)
# START_DATE = datetime(2022, 11, 1)
# END_DATE = datetime(2022, 12, 31)
#
# # ✅ 데이터를 저장할 딕셔너리 (월별 종가 리스트)
# monthly_prices = defaultdict(list)
#
# # ✅ 월별로 API 요청하여 데이터 가져오기
# current_date = START_DATE
#
# while current_date <= END_DATE:
#     year, month = current_date.year, current_date.month
#     first_day = datetime(year, month, 1)
#     last_day = (first_day + timedelta(days=31)).replace(day=1) - timedelta(days=1)
#
#     params = {
#         "serviceKey": API_KEY,
#         "numOfRows": "1000",
#         "pageNo": "1",
#         "likeSrtnCd": STOCK_CODE,
#     }
#
#     response = requests.get(BASE_URL, params=params, verify=certifi.where())
#
#     print(f"🔹 요청 URL: {response.url}")  # ✅ 요청 URL 확인
#     print(f"🔹 응답 상태 코드: {response.status_code}")  # ✅ 응답 상태 코드 확인
#     print(f"🔹 응답 본문 (텍스트): {response.text[:500]}")  # ✅ 응답 일부 확인
#
#     if response.status_code != 200:
#         print(f"❌ API 요청 실패: {response.status_code}")
#         break
#
#     root = ET.fromstring(response.text)
#     items = root.findall(".//item")
#
#     if not items:
#         print(f"❌ {year}-{month} 데이터 없음!")  # ✅ 데이터 없는 경우 확인
#
#     for item in items:
#         date_str = item.find("basDt").text
#         date_obj = datetime.strptime(date_str, "%Y%m%d")
#
#         closing_price = item.find("clpr")
#         if closing_price is None:
#             print(f"⚠️ {date_str} 데이터에 종가(clpr) 없음!")
#             continue
#
#         closing_price = float(closing_price.text)
#         monthly_prices[(year, month)].append(closing_price)
#
#     print(f"📌 {year}-{month} 종가 데이터: {monthly_prices[(year, month)]}")  # ✅ 데이터 확인
#
#     current_date = (first_day + timedelta(days=32)).replace(day=1)
#     time.sleep(1)
#
# monthly_avg_prices = []
# for (year, month), prices in sorted(monthly_prices.items()):
#     avg_price = round(sum(prices) / len(prices), 2)
#     monthly_avg_prices.append([year, month, avg_price])
#
# df = pd.DataFrame(monthly_avg_prices, columns=["Year", "Month", "Hyundai_Month_Stock_Average"])
# df.to_csv("hyundai_stock_monthly_avg.csv", index=False, encoding="utf-8-sig")
#
# print(f"\n✅ 현대차 월별 평균 종가 데이터가 'hyundai_stock_monthly_avg.csv' 파일로 저장되었습니다!")



#
# import requests
# import urllib.parse
# import certifi
#
# API_KEY = ""  # 👉 여기에 본인의 API 키를 입력하세요
# DECODED_API_KEY = urllib.parse.unquote(API_KEY)  # URL 디코딩 적용
#
# params = {
#     "serviceKey": DECODED_API_KEY,  # URL 디코딩된 API 키 사용
#     "resultType": "json",
#     "numOfRows": "1",
#     "pageNo": "1",
#     "basDt": "20231101",
#     "srtnCd": "005380",
# }
#
# response = requests.get(
#     "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo",
#     params=params,
#     verify=certifi.where(),  # SSL 검증 비활성화 (테스트용)
#     timeout=10
# )
#
# print("🔹 응답 상태 코드:", response.status_code)
# print("🔹 응답 본문 (텍스트):", response.text)  # 응답 내용을 직접 출력
#
# # JSON 디코딩 시도
# try:
#     data = response.json()
#     print("✅ JSON 응답 데이터:", data)
# except requests.exceptions.JSONDecodeError:
#     print("❌ JSON 디코딩 오류: 응답이 JSON 형식이 아닙니다.")