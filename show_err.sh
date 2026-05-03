#!/bin/bash

# Check if an argument was provided
if [ -z "$1" ]; then
    echo "Usage: $0 <test_id>"
    echo "Example: $0 9"
    exit 1
fi

# Pad the first argument to 3 digits (e.g., 9 -> 009)
# The 10# prefix ensures it's treated as a base-10 number
TEST_ID=$(printf "%03d" $((10#$1)))

# Run the diff using the padded ID
diff --side-by-side --color=always "test/$TEST_ID/expected.json" "test/$TEST_ID/out.json"
