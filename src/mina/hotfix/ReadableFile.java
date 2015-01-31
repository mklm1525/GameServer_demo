package mina.hotfix;

public class ReadableFile extends AbstractResFile{
	
	private Class<? extends Readable> clazz = null;
	
	public ReadableFile(String inter, String fileName, Class<? extends Readable> clazz){
		super(inter, fileName);
		this.clazz = clazz;
		this.reload();
	}

	@Override
	public boolean load(String inter) {
		String fileName = this.getFileName(); 
		if(fileName.endsWith("xls")){
			ResourceFileReader.getReader().parseXlsFile(inter, fileName, clazz);
		}
		else{
			ResourceFileReader.getReader().parseBinFile(inter, fileName, clazz);
		}
		return true;
	}
	
	@Override
	public boolean checkModifyAndLoad(){
		return false;
	}

}
