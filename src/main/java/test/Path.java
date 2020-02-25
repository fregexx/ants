package test;

import com.sun.istack.internal.Nullable;
import lombok.Data;

import java.util.Objects;

@Data
public class Path {
    private int a;
    private int b;
    private double distance;
    private double tau;

    public Path(int a, int b, double distance) {
        this.a = a;
        this.b = b;
        this.distance = distance;
        this.tau = 1;
    }

    @Override
    public int hashCode() {
        int aHash = a;
        int bHash = b;

        int min = Math.min(aHash, bHash);
        int max = Math.max(aHash, bHash);

        return Objects.hash(min, max);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Path path = (Path) o;

        return this.a == path.getA() && this.b == path.getB()
                || this.a == path.getB() && this.b == path.getA();
    }

    public boolean equals(int a, int b) {
        return this.a == a && this.b == b
                || this.a == b && this.b == a;
    }

    public double getValue(double beta, double alpha) {
        return Math.pow(1.0 / distance, beta) * Math.pow(tau, alpha);
    }
}
