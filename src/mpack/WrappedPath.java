package mpack;


import java.nio.file.Path;

public class WrappedPath implements Comparable<WrappedPath> {
    public final Path p;
    public final String name;
    public WrappedPath(Path p) {
        this.p = p;
        this.name = p.getFileName().toString().substring(0, p.getFileName().toString().lastIndexOf('.'));
    }

    @Override
    public int compareTo(WrappedPath vp) {
        return this.name.compareTo(vp.name);
    }
}
