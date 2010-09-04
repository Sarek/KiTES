package kites.visual;

public interface NodeContainer {
	public void setInterpreterWindow(InterpreterWindow wnd);
	public String toString();
	public void deactivate();
	public void addComma();
	public void addClosePar();
	public void activate();
	public void colorize();
}
