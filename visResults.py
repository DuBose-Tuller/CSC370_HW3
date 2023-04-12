import numpy as np
import pandas as pd

NUM_BOTS = 12

M = np.zeros((NUM_BOTS,NUM_BOTS))

M = [["" for i in range(NUM_BOTS)] for j in range(NUM_BOTS)]

with open("results.txt", "r+") as f:
    for i in range(NUM_BOTS*NUM_BOTS):
        p1wins = f.readline()
        ties = f.readline()
        p2wins = f.readline()

        result = int(str(p1wins).split(" ")[1]) - int(str(p2wins).split(" ")[1])
        if result > 0:
            M[i % NUM_BOTS][i//NUM_BOTS] = "W"
        elif result < 0:
            M[i % NUM_BOTS][i//NUM_BOTS] = "L"
        else:
            M[i % NUM_BOTS][i//NUM_BOTS] = "D"

df = pd.DataFrame(M)
df.to_csv("table.csv")