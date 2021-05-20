import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Controller implements ActionListener {
	
	public static void main(String[] args) {
		
		// Create a DataHandler object in a AbstractDHFactory reference
		// (getDataHandlerInstance creates the instance and allows only 1 to exist) 
		AbstractDataHandlerFactory dataHandlerFactory = DataHandler.getDataHandlerInstance(); 
		
		// Create a Controller object to begin the application and control code flow 
		Controller control = new Controller(dataHandlerFactory);	
	}
	
	// Controller attributes
	private UserInterface view = null;
	private AbstractDataHandlerFactory data; 
	String filepathAndName; 
	
	
	// Constructor 
	public Controller (AbstractDataHandlerFactory dataHandlerFactory) {
	
		// Set view
		view = new UserInterface(this);
		view.setVisible(true);
		
		// Set the data handler
		this.data = dataHandlerFactory; 
		
		
		
		// Load data for application 
		filepathAndName = "C:\\Users\\chpas\\git\\PTT-System\\PTTAppData.txt";
		data.loadData(filepathAndName);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//1-GET TO ADMIN VIEW
		if (e.getSource() == view.adminButton) {
			System.out.println("view as Admin");
			view.updateView(view.adminMain);
		}
		
		//2-GET TO CD VIEW
		else if (e.getSource() == view.courseButton) {
			System.out.println("view as Course Director");
			view.updateView(view.cDPanel);
		}
		
		//3 - EXIT BUTTON
		else if (e.getSource() == view.exitButton) {
			//write to file?
			data.saveData(filepathAndName);
			System.exit(0);
		}
		
		//3.2 BACK BUTTON FOR BOTH ADMIN AND CD
		else if (e.getSource() == view.adminMain.backButton || e.getSource() == view.cDPanel.backButton) {
			if (e.getSource() == view.adminMain.backButton) view.backToMain(view.adminMain);
			else view.backToMain(view.cDPanel);
		}
		
		//1.1 ADMIN >> ASSIGN TEACHER TO REQUEST
		else if (e.getSource() == view.adminMain.assign) {		
			view.adminMain.textArea.setText("");
				String name = view.adminMain.teachName.getText();
				int iD = Integer.parseInt(view.adminMain.requestNo.getText()); 
				TeachRequest t = data.getLOR().findReq(iD);
				PTTeacher p = data.getLOP().getTeacherRef(name);
				
				boolean outcome = false;
				if (t == null || p == null) {
					view.adminMain.textArea.setText("invalid request number or teacher name");
				}
				else {
					t.addTeacher(p);
					outcome = p.assign(t);
				
				}
				if (!outcome) {
					view.adminMain.textArea.setText("assignment failed");
				}
		}
		
		// 1.2 - ADMIN >> SEARCH TEACHERS BY DIFFERENT CRITERIA
		else if (e.getSource() == view.adminMain.searchButton) {
			
			view.adminMain.textArea.setText("");
			
			int i = view.adminMain.optionList.getSelectedIndex();
			
			System.out.println("search - int:" + i );
			
			String s = view.adminMain.searchChoiceOne.getText().trim();
			
			ArrayList <PTTeacher> result = data.getLOP().findTeacher(s, i);
			
			for (PTTeacher p : result) {
				view.adminMain.textArea.append(p.toString());
			}
			if (result.size() == 0) {
				view.adminMain.textArea.setText("no results");
			}
			
			//view.adminMain.searchF.setEnabled(false);
		}
		
		//1.3 ADMIN >> VIEW REQUESTS
		else if (e.getSource() == view.adminMain.viewReqs) {
			
			System.out.println("view reqs");
			view.adminMain.textArea.setText("");
			String [] s = data.getLOR().printReqList();
			for (String i : s) {
				view.adminMain.textArea.append(i);
				System.out.println(i);
			}

		}
		
		//1.4 ADMIN >> UPDATE INFORMATION FOR A SPECIFIC TEACHER
		//1.4.1 ADMIN >> add skill/training
		else if (e.getSource() == view.adminMain.addSkill) {
			view.adminMain.textArea.setText("");
			
			int iD = Integer.parseInt(view.adminMain.teachID.getText());
			PTTeacher t = data.getLOP().getTeacherRef(iD);
			int n = view.adminMain.optionListUpdate.getSelectedIndex();
			String s = view.adminMain.choice.getText().trim();
				
			if (n == 0) {
				t.addSkill(s);				
			}
			else if(n== 1) {
				t.addTraining(s);
			}
		}
		//1.4.2 ADMIN >> remove skill/training	
		else if (e.getSource() == view.adminMain.remSkill) {
			view.adminMain.textArea.setText("");
			int iD = Integer.parseInt(view.adminMain.teachID.getText());
			PTTeacher t = data.getLOP().getTeacherRef(iD);
			int n = view.adminMain.optionListUpdate.getSelectedIndex();
			String s = view.adminMain.choice.getText().trim();
			
			if (n == 0) {
				t.removeSkill(s);
			}
			else if (n ==1 ) {
				t.removeTraining(s);
			}
		}
		
		//1.5 ADMIN >> view list of teachers
		else if (e.getSource() == view.adminMain.viewPTT) {
			 
			view.adminMain.textArea.setText("");
			
			for (PTTeacher p : data.getLOP().getListReference()) {
				String s = p.toString();
				view.adminMain.textArea.append(s);
			}
		}
		
		//1.6 ADMIN >> add teacher to the system
		
	
		//2.1 CD >> VIEW STATUS OF REQUESTS IN THE SYSTEM
		else if (e.getSource() == view.cDPanel.statCheck) {
			System.out.println("view reqs status");
			view.cDPanel.displayField.setText("");
			String [] s = data.getLOR().printReqListStatus();
			for (String i : s) {
				System.out.println(i);
				view.cDPanel.displayField.append(i);
			}
		}
		
		//2.2 CD >> ADD A REQUEST TO THE SYSTEM MANUALLY
		else if (e.getSource() == view.cDPanel.submitReq) {
			view.cDPanel.displayField.setText("");
			String n = view.cDPanel.courseName.getText();
			int i = Integer.parseInt(view.cDPanel.noReq.getText());
			String s = view.cDPanel.skills.getText();
			String [] skills = s.split(",");
			TeachRequest r = new TeachRequest (n,i,data.getLOR(), skills);
			
			view.cDPanel.displayField.setText("new request created: \n"+ r.toString());
		}
	}
}
