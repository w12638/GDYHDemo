package entities;
/**
 * 返回数据工具类
 * @author user
 *
 */
public class DataReturn {
    private String code;
    private String message;
    private Object data;
    
    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public DataReturn(String code, String message,Object data){
		this.code = code;
		this.message = message;
		this.data = data;
    }

	public DataReturn( String message,Object data){
        this("0000",message,data);
    }

}
