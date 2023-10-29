package fr.thedarven.utils.helpers;

import fr.thedarven.TaupeGun;

import java.io.*;
import java.util.Objects;

public class FileHelper<T>{

    private final TaupeGun main;
    private final String fileName;

    public FileHelper(TaupeGun main, String fileName) {
        this.main = main;
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public T readFile() {
        createFile();
        T element = null;
        ObjectInputStream is = null;

        try {
            is = new ObjectInputStream(new FileInputStream(this.main.getDataFolder()+"/"+fileName));
            element = (T) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (Objects.nonNull(is)) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return element;
    }

    public void writeFile(T element) {
        writeFile(element, true);
    }

    private void createFile() {
        try {
            File file = new File(this.main.getDataFolder(), fileName);
            if (!file.exists()) {
                file.createNewFile();
                writeFile(null, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(T element, boolean withFileVerification) {
        if (withFileVerification) {
            createFile();
        }

        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(new FileOutputStream(this.main.getDataFolder() + "/" + fileName));
            os.writeObject(element);
            os.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        if (Objects.nonNull(os)) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
