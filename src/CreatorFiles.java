import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

public class CreatorFiles {

    Logger logger;

    private String gamesDirectory;

    private CreatorFiles(Logger logger) {
        this.logger = logger;
    }


    public static void make(Logger logger, Consumer<CreatorFiles> block) {
        CreatorFiles maker = new CreatorFiles(logger);

        try {
            block.accept(maker);
        } finally {
            System.out.println(logger.getLog());
        }
    }


    public CreatorFiles setGamesDirectory(String path) {
        this.logger.info(String.format("Установка Игры в %s", path));
        this.gamesDirectory = new File(path).getAbsolutePath();
        return this;
    }


    public CreatorFiles makeDirectoryToGames(String path) {
        String fullPath = gamesDirectory + path;

        this.logger.info(String.format("Создание каталога: %s", fullPath));
        File dir = new File(fullPath);

        if (dir.exists()) {
            this.logger.info("Каталог уже существует");
            return this;
        }

        boolean result = dir.mkdir();
        if (!result) {
            this.logger.info("Каталог не был создан");
            return this;
        }

        this.logger.info("Каталог создан");

        return this;
    }


    public CreatorFiles createFileToGames(String path) {
        String fullPath = gamesDirectory + path;

        this.logger.info(String.format("Создание файла: %s", fullPath));
        File file = new File(fullPath);

        if (file.exists()) {
            this.logger.info("Файл уже существует");
            return this;
        }

        boolean result = false;
        try {
            result = file.createNewFile();
        } catch (IOException e) {
            this.logger.info(String.format("Ошибка при создании файла: %s", e.getMessage()));
        } finally {
            if (!result) {
                this.logger.info("Файл не был создан");
                return this;
            }
        }

        this.logger.info("Файл был создан");

        return this;
    }


    public CreatorFiles saveLogToFile(String path) {
        String fullPath = gamesDirectory + path;

        this.logger.info(String.format("Журнал сохранён в: %s", fullPath));
        File file = new File(fullPath);

        if (!file.exists()) {
            this.logger.info("Файл не существует");
            return this;
        }

        if (!file.canWrite()) {
            this.logger.info("Файл недоступен для записи в журнал");
            return this;
        }

        boolean result = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.append(logger.getLog());
        } catch (IOException e) {
            this.logger.info(String.format("Ошибка при сохранении журнала: %s", e.getMessage()));
            return this;
        }

        this.logger.info("Журнал сохранен успешно");

        return this;
    }
}