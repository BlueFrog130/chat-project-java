import pandas as pd

df = pd.read_csv("500u_1s.csv")

print(df["rtt"].median())
