import numpy as np
import matplotlib.pyplot as plt

#SHOW = 'linear'
SHOW = 'logarithmic'

def lmt_linear(x, T, F):
    if x <= T:
        return x
    else:
        return F * (x - T) + T

def lmt_logarithmic(x, T, F):
    if x <= T:
        return x
    else:
        return np.log(x - T + 1 / np.log(F)) / np.log(F) + T - np.log(1 / np.log(F)) / np.log(F)

x_values = np.linspace(0, 100, 100000)
if SHOW == 'linear':
    T = 30.0
    F = 0.25
    y_values = np.array([lmt_linear(x, T, F) for x in x_values])
elif SHOW == 'logarithmic':
    T = 30.0
    F = 1.5
    y_values = np.array([lmt_logarithmic(x, T, F) for x in x_values])

plt.figure(figsize=(8, 6))
plt.plot(x_values, y_values, label='Damage', color='blue')
plt.axhline(y=T, color='red', linestyle='--', label='Threshold')
plt.xlabel('Raw damage')
plt.xlim(0, 100)
plt.ylabel('Processed damage')
plt.ylim(0, 50)
plt.title(f'Damage limiting with threshold={T} and factor={F}')
plt.legend()
plt.grid(True)
plt.show()
