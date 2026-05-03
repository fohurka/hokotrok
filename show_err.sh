#!/bin/bash

# Check if an argument was provided
if [ -z "$1" ]; then
    echo "Usage: $0 <test_id>"
    echo "Example: $0 009"
    exit 1
fi

# Run the diff with colors enabled
# --color=always ensures it stays colorful even inside the script environment
diff --side-by-side --color=always "test/$1/expected.json" "test/$1/out.json"
