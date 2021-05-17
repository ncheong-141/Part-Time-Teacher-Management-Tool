import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
	private DataHandler currentHandler = null;
	private UserInterface view = null;
	
	public Controller (UserInterface v) {
		//currentHandler = d;
		view = v;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == view.adminButton) {
			System.out.println("view as Admin");
			view.updateView(view.getAdminMain());
		}
		
		else if (e.getSource() == view.courseButton) {
			System.out.println("view as Admin");
			view.updateView(view.getCDPanel());
		}
		
		
		else if (e.getSource() == view.adminMain.assignTeach) {
			
		}
		
		else if (e.getSource() == view.adminMain.search) {
			
		}
		
		else if (e.getSource() == view.adminMain.viewReqs) {
			
		}
		
		else if (e.getSource() == view.adminMain.updateTeach) {
			
		}
	}
}
