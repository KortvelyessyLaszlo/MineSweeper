import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class GameFrame extends JFrame implements WindowListener{
	private static final long serialVersionUID = -8519965885188987594L;
	
	private ArrayList<ArrayList<GameButton>> buttons = new ArrayList<ArrayList<GameButton>>();
	private int width;
	private int height;
	private Difficulty diff;
	private boolean isOver = false;
	private Random random = new Random();
	
	GameFrame(Difficulty choice){
		super("MineSweeper");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 500);
		setResizable(true);
		addWindowListener(this);
		
		diff = choice;
		if(diff.equals(Difficulty.Beginner)) {
			width = 8;
			height = 8;	
		}else if(diff.equals(Difficulty.Intermediate)) {
			width = 16;
			height = 16;
		}else if(diff.equals(Difficulty.Expert)) {
			width = 30;
			height = 16;
		}
		
		setLayout(new GridLayout(width, height));
		
		for(int i = 0; i < width; ++i) {
			buttons.add(new ArrayList<GameButton>());
			for(int j = 0; j < height; ++j) {
				buttons.get(i).add(new GameButton());	
				add(buttons.get(i).get(j));			
			}
		}
		
		addMouseListeners();
		initMines();
	}
	
	public boolean isOver() {
		return isOver;
	}
	
	public void addMouseListeners() {
		for(int i = 0; i < width; ++i) {
			for(int j = 0; j < height; ++j) {
				buttons.get(i).get(j).addMouseListener(new GameButtonListener(i,j));			
			}
		}
	}
	
	public void initMines() {
		int numberOfMines = 0;
		
		if(diff.equals(Difficulty.Beginner)) {
			numberOfMines = 10;
		}else if(diff.equals(Difficulty.Intermediate)) {
			numberOfMines = 40;
		}else if(diff.equals(Difficulty.Expert)) {
			numberOfMines = 99;
		}
		
		while(numberOfMines > 0) {
			for(int i = 0; i < buttons.size() && numberOfMines > 0; ++i) {
				for(int j = 0; j < buttons.get(i).size() && numberOfMines > 0; ++j) {
					GameButton button = buttons.get(i).get(j);
					
					if(button.isBomb())
						continue;
					
					if(random.nextInt(10) == 1) {
						button.setBomb(true);
						--numberOfMines;
					}
				}
			}
		}
	}
	
	
	public class GameButtonListener extends MouseAdapter {
		private int x;
		private int y;
		
		
		GameButtonListener(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public void mouseClicked(MouseEvent e) {
			GameButton button = (GameButton)e.getSource();
			
			if(SwingUtilities.isRightMouseButton(e)) {
				if(button.isRevealed()) 
					return;
				
				if(button.isFlagged()) {
					button.setFlagged(false);
					button.setText("");
				} else {
					button.setFlagged(true);
					button.setText("!");
				}
			}
			
			if(SwingUtilities.isLeftMouseButton(e)) {
				if(button.isRevealed())
					return;
				
				if(button.isFlagged())
					return;
				
				if(button.isBomb()) {
					isOver = true;
					JOptionPane.showMessageDialog(null, "Game Over! you clicked on a mine.");
					dispose();
				}
			
				revealButton(x,y);
				
				if(gameIsWon()) {
					isOver = true;
					JOptionPane.showMessageDialog(null, "Congratulations! You Won.");
					dispose();
				}
			}
		}	
	}
	
	public boolean gameIsWon() {
		int cnt = 0;
		
		for(ArrayList<GameButton> list : buttons) {
			for(GameButton button : list) {
				if(button.isRevealed() || button.isBomb()) 
					++cnt;
			}
		}
		return (cnt == (width * height)); 
	}
	
	public int countAdjacentMines(int x, int y) {
		int sum = 0;
		
		for(int i = Math.max(0, x - 1); i <= Math.min(width - 1, x + 1); ++i) {
			for(int j = Math.max(0, y - 1); j <= Math.min(height - 1 , y + 1); ++j) {
				if(buttons.get(i).get(j).isBomb())
					++sum;
			}
		}
		
		return sum;
	}
	
	public void revealButton(int x, int y) {
		
		if( x < 0 || x >= width || y < 0 || y >= height || !buttons.get(x).get(y).isEnabled())
			return;
		
		GameButton button = buttons.get(x).get(y);
		
		button.setEnabled(false);
		button.setRevealed(true);
		
		if(countAdjacentMines(x,y) == 0) {
			
			for(int i = x - 1; i <= x + 1; ++i) {
				for(int j = y - 1; j <= y + 1; ++j) {
					revealButton(i,j);
				}	
			}
		}else
			button.setText(Integer.toString(countAdjacentMines(x,y)));		
	}
	
	public void save() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.txt"));
			out.writeObject(this);
			out.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
	}
	
	@Override
	public void windowOpened(WindowEvent e) {		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		save();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		save();
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
