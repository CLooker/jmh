package com.clooker;

import org.eclipse.collections.impl.set.mutable.UnifiedSet;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class SetsBenchmark {

  @Param({"100", "1000", "10000"})
  private int intsSize;

  private List<Integer> ints;

  public static void main(String[] args) throws RunnerException {
    final var options =
        new OptionsBuilder()
            .include(SetsBenchmark.class.getSimpleName())
            .forks(1)
            .warmupIterations(5)
            .result("result.csv")
            .resultFormat(ResultFormatType.CSV)
            .build();

    new Runner(options).run();
  }

  @Setup
  public void setup() {
    final ArrayList<Integer> temp = new ArrayList<>(intsSize);
    while (temp.size() < intsSize) {
      final var nextInt = ThreadLocalRandom.current().nextInt();
      temp.add(nextInt);
    }
    ints = temp;
  }

  @Benchmark
  public void hashSet_add(Blackhole bh) {
    final Set<Integer> hashSet = new HashSet<>();
    ints.forEach(hashSet::add);
    bh.consume(hashSet);
  }

  @Benchmark
  public void unifiedSet_add(Blackhole bh) {
    final Set<Integer> unifiedSet = new UnifiedSet<>();
    ints.forEach(unifiedSet::add);
    bh.consume(unifiedSet);
  }
}
