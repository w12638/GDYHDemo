package entities;
/**
 * 返回数据工具类
 * @author user
 *
 */
public class DataReturn<T> {
    private String code;
    private String message;
    private T data;
    
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

	public void setData(T data) {
		this.data = data;
	}
	
	public DataReturn(){
		
    }
	
	public DataReturn(String code, String message,T data){
		this.code = code;
		this.message = message;
		this.data = data;
    }

	public DataReturn( String message,T data){
        this("000000",message,data);
    }

}
