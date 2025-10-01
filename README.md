# Assignment 1 – Divide and Conquer Algorithms

## Overview
This project implements and analyzes four classical divide-and-conquer algorithms:

- **MergeSort** with cutoff and reusable buffer
- **QuickSort** with randomized pivot and smaller-first recursion (bounded stack)
- **Deterministic Select** (Median-of-Medians, O(n))
- **Closest Pair of Points** (plane-sweep, O(n log n))

The implementation is written in Java (Maven project).  
Metrics (runtime, comparisons, swaps, allocations, recursion depth) are collected and exported to CSV.  
Graphs are plotted to compare theoretical and experimental performance.

---

## Architecture and Design Choices

1. **MergeSort**
    - Uses a single reusable buffer to reduce allocations.
    - Switches to Insertion Sort for small subarrays (cutoff optimization).
    - Recursion depth bounded by log₂(n).

2. **QuickSort**
    - Random pivot to avoid worst-case inputs.
    - Always recurses on the smaller partition, while processing the larger iteratively.
    - This keeps stack depth at O(log n) instead of O(n).

3. **Deterministic Select (Median-of-Medians)**
    - Splits input into groups of 5, finds medians, then recursively selects pivot.
    - Guarantees linear runtime in worst case.
    - Implemented with careful partitioning to avoid extra memory.

4. **Closest Pair of Points**
    - Standard divide-and-conquer: sort by x, split, solve recursively, merge via y-sorted strip.
    - Only a constant number (≤7) of points in strip need to be checked per point.
    - Complexity O(n log n), but large constants due to extra sorting and data structures.

---

## Recurrence Analysis

**MergeSort**
- Recurrence: T(n) = 2T(n/2) + Θ(n)
- Master Theorem → T(n) = Θ(n log n)
- Depth: log₂(n), confirmed ~14 for n=200k.

**QuickSort (randomized, smaller-first recursion)**
- Expected recurrence: T(n) = 2T(n/2) + Θ(n) → Θ(n log n) on average
- Random pivot avoids worst case; smaller-first ensures stack = O(log n)
- Depth observed ~9 at n=200k.

**Deterministic Select (Median-of-Medians)**
- Recurrence: T(n) = T(n/5) + T(7n/10) + Θ(n)
- By Akra–Bazzi: T(n) = Θ(n)
- Depth ~0, no recursion growth observed.

**Closest Pair of Points**
- Recurrence: T(n) = 2T(n/2) + Θ(n)
- Master Theorem → Θ(n log n)
- Depth ~log₂(n), observed ~17 for n=200k.

---

## Experimental Results

### Raw Table (n=200000)

| algo         | n      | millis | comparisons | swaps   | allocations | maxDepth |
|--------------|-------:|-------:|------------:|--------:|------------:|---------:|
| mergesort    | 200000 | 25.85  | 3,730,745   | 907,209 | 200,000     | 14 |
| quicksort    | 200000 | 31.89  | 3,299,342   | 1,359,681 | 0          | 9 |
| select_MoM5  | 200000 | 11.51  | 1,665,532   | 1,439,896 | 0          | 0 |
| closest_pair | 200000 | 504.56 | 5,588,505   | 0        | 4,797,986   | 17 |

---

### Runtime vs n
docs/plots/runtime_vs_n.png

### Recursion Depth vs n
docs/plots/depth_vs_n.png

### Detailed Metrics (n=200000)
- **Runtime (ms)**  
  docs/plots/time_ms_200k.png

- **Comparisons**  
  docs/plots/comparisons_200k.png

- **Max Recursion Depth**  
  docs/plots/depth_200k.png

- **Allocations**  
  (docs/plots/allocations_200k.png

---

## Discussion

- **Select (MoM5)** is the fastest for finding order statistics: ~11.5 ms for n=200k, scaling linearly as predicted.
- **MergeSort vs QuickSort**: both Θ(n log n). MergeSort was faster here (~25 ms vs 32 ms), largely due to fewer swaps, but performed more comparisons.
- **QuickSort** achieved shallower recursion depth (9 vs 14) thanks to the smaller-first strategy.
- **Closest Pair** was significantly slower (~500 ms), despite being O(n log n), due to heavy allocations and constant factors.
- Depth growth for all algorithms matches theoretical log₂(n) expectations.

---

## Conclusion

- Implementations align with theoretical expectations.
- Metrics (comparisons, swaps, allocations, recursion depth) give deeper insight beyond runtime.
- The experiments confirm that algorithm design choices (e.g., pivot selection, buffer reuse) significantly affect constant factors.
- Future work: run on larger datasets and analyze cache behavior / GC effects.