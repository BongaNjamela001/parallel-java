import subprocess
import re
import sys
from collections import Counter

if len(sys.argv) != 8:
    print("Usage: python script_name.py row column xmin xmax ymin ymax searches_density")
    sys.exit(1)

java_files = [
    "Search.java",
    "TerrainArea.java",
    "MonteCarloMinimization.java",
    "MonteCarloRecursiveTask.java",
    "MonteCarloForkJoinApplication.java",
    "MonteCarloCommonPool.java",
    "MonteCarloCommonPoolApp.java",
    "MonteCarloMultipleForks.java",
    "MonteCarloMultipleDivisionApp.java",
]

num_runs = 50
output_times = []

row = sys.argv[1]
column = sys.argv[2]
xmin = sys.argv[3]
xmax = sys.argv[4]
ymin = sys.argv[5]
ymax = sys.argv[6]
searches_density = sys.argv[7]

with open("Serial_Grid_12500x12500.txt", "w") as f:
    for _ in range(num_runs):
        #Compile all java files
        subprocess.run(["javac"] + java_files, check=True)
        
        #Run the Java program and capture the output
        result = subprocess.run(["java", "MonteCarloMini.MonteCarloMinimization", row, column, xmin, xmax, ymin, ymax, searches_density], capture_output=True, text=True)
        
        #Get the 4th line of the output, corresponding to index 3, and read the time
        match = re.search(r"Time: (\d+) ms", result.stdout)
        if match:
            time = float(match.group(1))
            output_times.append(time)
            #output to file
            f.write(str(time) + "\n")

worst_time = max(output_times)
best_time = min(output_times)
mode_time = Counter(output_times).most_common(1)[0][0]

average_time = sum(output_times)/num_runs

print(f"Worst time: {worst_time}")
print(f"Mode time: {mode_time}")
print(f"Best time: {best_time}")
print(f"Total average time: {average_time}")