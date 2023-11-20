import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MenuFrame extends JFrame{
	
	private static final long serialVersionUID = 6060922250135356036L;
	
	private JButton continueGame = new JButton("Continue Game");
	private JButton newGame = new JButton("New Game");
	Difficulty diff[] = { Difficulty.Beginner, Difficulty.Intermediate, Difficulty.Expert};
	private JComboBox<Difficulty> difficultyBox = new JComboBox<>(diff);
	private GameFrame game;
	
	MenuFrame() throws FileNotFoundException, ClassNotFoundException, IOException{
		super("GameMenu");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(300, 200);
		setResizable(true);
		setLayout(new GridLayout(2,1));
		
		JPanel north = new JPanel();
		JPanel south = new JPanel();
		
		north.setLayout(new FlowLayout(FlowLayout.CENTER,0,30));
		south.setLayout(new FlowLayout(FlowLayout.CENTER,0,30));
		
		add(north);
		add(south);
		
		north.add(newGame);
		north.add(difficultyBox);
		south.add(continueGame);
		
		continueGame.addActionListener(new ContinueButtonListener());
		newGame.addActionListener(new newGameButtonListener());
		
		game = loadGame();
		if(game.isOver())
			continueGame.setEnabled(false);
	}
	
	public GameFrame loadGame() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.txt"));
		GameFrame game = (GameFrame)in.readObject();
		in.close();
		game.addMouseListeners();
		return game;
	}
	
	class ContinueButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent ae) {
			game.setVisible(true);
			setVisible(false);
		}	
	}
	
	class newGameButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			game = new GameFrame((Difficulty)difficultyBox.getSelectedItem());
			game.setVisible(true);
			setVisible(false);
		}
		
	}
}
