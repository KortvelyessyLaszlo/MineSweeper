import javax.swing.JButton;

public class GameButton extends JButton{
	private static final long serialVersionUID = -8000120374609856168L;
	
	private boolean isBomb = false;
	private boolean isFlagged = false;
	private boolean isRevealed = false;
	
	public boolean isBomb() {
		return isBomb;
	}
	
	public void setBomb(boolean value) {
		isBomb = value;
	}
	
	public boolean isFlagged() {
		return isFlagged;
	}
	
	public void setFlagged(boolean value) {
		isFlagged = value;
	}
	
	public boolean isRevealed() {
		return isRevealed;
	}
	
	public void setRevealed(boolean value) {
		isRevealed = value;
	}
	
}
