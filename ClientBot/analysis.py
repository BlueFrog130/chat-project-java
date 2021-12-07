import pandas as pd
from glob import glob
from os.path import splitext, basename
import matplotlib.pyplot as plt
from scipy.stats import zscore
from numpy import abs

files = glob("data/remote/*.csv")


def to_df(file):
    name, _ = splitext(basename(file))
    users, interval = name.split("_")
    df = pd.read_csv(file)
    df["users"] = int(users[:-1])
    df["interval"] = float(interval[:-1])
    return df


df = pd.concat(map(to_df, files)) \
    .drop(columns=["id"])

df = df[(abs(zscore(df["rtt"])) < 3)]

df.boxplot(by=["users"], column="rtt",
           rot=45, figsize=(10, 10), fontsize=12)

plt.suptitle("")
plt.title("Remote RTT by users")
plt.ylabel("RTT (ms)")
plt.xlabel("Users")

plt.show()
