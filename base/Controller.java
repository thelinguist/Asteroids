package edu.byu.cs.superasteroids.base;

import edu.byu.cs.superasteroids.database.DAO;

public class Controller implements IController{
	private DAO dao = DAO.getInstance();
	//if game data importer, put asteroids into DAO here

	private IView view;

	
	public Controller(IView view) {
		this.view = view;
	}

	@Override
	public IView getView() {
		return view;
	}

	@Override
	public void setView(IView view) {
		this.view = view;
	}
	
	
}
