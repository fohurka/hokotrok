#!/bin/bash

TEST_DIR="test"
PROGRAM_NAME="hokotrok"

passed=0
failed=0
total=0
failed_tests=()

echo "======================================="
echo "       JAVA TEST RUNNER ENGINE         "
echo "======================================="
echo ""

if [ ! -d "$TEST_DIR" ]; then
    echo "Error: Directory '$TEST_DIR' not found."
    exit 1
fi

# Make sure we have the latest compiled code in the build directory
javac src/main/java/*.java -d build

for dir in "$TEST_DIR"/*/; do
    dir_name=$(basename "$dir")
    
    if [ -f "$dir/commands.txt" ]; then
        ((total++))
        # Using printf to keep the dots and brackets aligned
        printf " Running Test %-15s " "[$dir_name]..."
        
        # Run using the hokotrok wrapper and capture output
        output=$(./$PROGRAM_NAME < "$dir/commands.txt" 2>&1)
        
        if echo "$output" | grep -q "SUCCESS"; then
            echo "[ SUCCESS ]"
            ((passed++))
        else
            echo "[ FAILURE ]"
            ((failed++))
            failed_tests+=("$dir_name")
        fi
    fi
done

echo ""
echo "+-------------------------------------+"
echo "|  TEST RESULTS SUMMARY               |"
echo "+-------------------------------------+"
printf "| Total Tests Run: %-18d |\n" "$total"
printf "| Passed:          %-18d |\n" "$passed"
printf "| Failed:          %-18d |\n" "$failed"
echo "+-------------------------------------+"

if [ ${#failed_tests[@]} -ne 0 ]; then
    echo ""
    echo "Failed Cases:"
    for t in "${failed_tests[@]}"; do
        echo " - $t"
    done
    echo ""
fi

[ $failed -gt 0 ] && exit 1 || exit 0