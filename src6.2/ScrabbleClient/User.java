package ScrabbleClient;

public class User {
	
	private String userId;
	private int score;
	public User(String newuser) {
		userId=newuser;
		score=0;
	}
	public int getScore() {
		return score;
	}
	public String getUserId() {
		
		return userId;
	}

}
