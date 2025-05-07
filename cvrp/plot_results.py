import os
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

def plot_results(results_folder):
    plots_folder = os.path.join(results_folder, "plots")
    
    # Create the plots folder if it doesn't exist
    if not os.path.exists(plots_folder):
        os.makedirs(plots_folder)
    
    # Get all CSV files in the results folder
    csv_files = [f for f in os.listdir(results_folder) if f.endswith(".csv")]
    
    # Combine all CSV files into a single DataFrame
    combined_data = []
    for csv_file in csv_files:
        file_path = os.path.join(results_folder, csv_file)
        print(f"Processing file: {file_path}")
        
        # Read the CSV file
        df = pd.read_csv(file_path)
        df["TestInstance"] = csv_file.replace(".csv", "")  # Add a column for the test instance name
        combined_data.append(df)
    
    # Concatenate all data into a single DataFrame
    combined_df = pd.concat(combined_data, ignore_index=True)
    
    # Plot grouped data
    plt.figure(figsize=(12, 8))
    sns.barplot(
        data=combined_df,
        x="TestInstance",
        y="TotalDistance",
        hue="Heuristic",
        palette="tab10"  # Use a distinct color palette
    )
    plt.title("Total Distance by Test Instance and Heuristic")
    plt.ylabel("Total Distance")
    plt.xlabel("Test Instance")
    plt.xticks(rotation=45)
    plt.legend(title="Heuristic")
    plt.tight_layout()
    
    # Save the plot in the plots folder
    plot_file = os.path.join(plots_folder, "grouped_results_plot.png")
    plt.savefig(plot_file)
    print(f"Grouped plot saved to: {plot_file}")
    plt.close()

if __name__ == "__main__":
    results_folder = "c:\\Users\\tomju\\OneDrive\\Desktop\\uni\\AE\\results"
    plot_results(results_folder)