import os
import shutil
import random

def split_dataset(source_dir, train_dir, val_dir, split_ratio=0.8):
    for root, dirs, files in os.walk(source_dir):
        for dir_name in dirs:
            source_folder = os.path.join(root, dir_name)
            train_folder = os.path.join(train_dir, dir_name)
            val_folder = os.path.join(val_dir, dir_name)

            # Create target directories if they don't exist
            os.makedirs(train_folder, exist_ok=True)
            os.makedirs(val_folder, exist_ok=True)

            # Get all file names in the directory
            files = os.listdir(source_folder)
            files = [f for f in files if os.path.isfile(os.path.join(source_folder, f))]
            
            # Shuffle the files
            random.shuffle(files)
            
            # Define split index
            split_index = int(len(files) * split_ratio)

            # Split files
            train_files = files[:split_index]
            val_files = files[split_index:]

            # Move files to the respective directories
            for file_name in train_files:
                shutil.move(os.path.join(source_folder, file_name), os.path.join(train_folder, file_name))
            
            for file_name in val_files:
                shutil.move(os.path.join(source_folder, file_name), os.path.join(val_folder, file_name))

source_directory = 'data/max/original'
train_directory = 'data/max/train'
val_directory = 'data/max/val'

split_dataset(source_directory, train_directory, val_directory)
