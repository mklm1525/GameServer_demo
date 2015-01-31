package mina.hotfix;

public class HotfixableFile extends AbstractResFile{
	private Class<? extends Hotfixable> clazz = null;
	
	public HotfixableFile(String inter, String fileName, Class<? extends Hotfixable> clazz){
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
}
