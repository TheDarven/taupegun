package fr.thedarven.utils.texts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import fr.thedarven.main.TaupeGun;

public class ReadFile{

	private String fileName;
	
	public ReadFile(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public String readFile() {
		File file = openFile();
		StringBuilder contentBuilder = new StringBuilder();
		String currentLine;
		BufferedReader br = null;
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
			while((currentLine = br.readLine()) != null) {
				contentBuilder.append(currentLine);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				fis.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return contentBuilder.toString();
	}	
	
	private File openFile() {
		try {
			File file = new File(TaupeGun.getInstance().getDataFolder(), fileName);
			if(!file.exists()) {
				file.createNewFile();
			}
			return file;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/* private void writeFile(T element, boolean withFileVerification) {
		if(withFileVerification)
			createFile();
		ObjectOutputStream os = null;
		try{
            os = new ObjectOutputStream(new FileOutputStream(MuchuPlugin.getInstance().getDataFolder()+"/"+fileName));
            os.writeObject(element);
            os.close();
        }catch (IOException e){
        	e.printStackTrace();
        }
		
		if(os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}	
	} */
	
}
