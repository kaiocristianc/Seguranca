package utils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public class Monitor extends Observable {

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private final boolean recursive;
    private boolean trace = false;

    public Monitor(Path dir, boolean recursive) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        this.recursive = recursive;
        if (recursive) {
            registerAll(dir);
        } else {
            register(dir);
        }
        this.trace = true;
    }

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    private void sinalizarMudancaArquivo(String arquivoOuPasta, String tipoEvento) {
        Map<String, String> mapa = new HashMap<String, String>();
        mapa.put("endereco", arquivoOuPasta);
        mapa.put("evento", tipoEvento);
        this.setChanged();
        this.notifyObservers(mapa);
    }

    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.println("PROBLEM!Entrou no REGISTRO");
                sinalizarMudancaArquivo(dir.toString(), "REGISTRO");
            } else {
                if (!dir.equals(prev)) {
                    System.out.println("PROBLEM!Entrou no UPDATE");
                    sinalizarMudancaArquivo(dir.toString(), "UPDATE");
                }
            }
        }
        keys.put(key, dir);
    }

    private void registerAll(final Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
