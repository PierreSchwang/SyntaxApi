package com.syntaxphoenix.syntaxapi.command;

import com.syntaxphoenix.syntaxapi.exceptions.ObjectLockedException;

public class CommandProcess {
	
	private final CommandManager manager;
	
	private boolean locked = false;
	private boolean valid = false;
	private BaseCommand command;
	private Arguments arguments;
	private String label;
	
	public CommandProcess(CommandManager manager) {
		this.manager = manager;
	}
	
	public CommandProcess setValid(boolean valid) {
		if(!isLocked()) {
			this.valid = valid;
		} else {
			throw locked();
		}
		return this;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public CommandProcess setLabel(String label) {
		if(!isLocked()) {
			this.label = label;
		} else {
			throw locked();
		}
		return this;
	}
	
	public String getLabel() {
		return label;
	}
	
	public CommandProcess setCommand(BaseCommand command) {
		if(!isLocked()) {
			this.command = command;
		} else {
			throw locked();
		}
		return this;
	}
	
	public BaseCommand getCommand() {
		return command;
	}
	
	public CommandProcess setArguments(Arguments arguments) {
		if(!isLocked()) {
			this.arguments = arguments;
		} else {
			throw locked();
		}
		return this;
	}
	
	public Arguments getArguments() {
		return arguments;
	}
	
	public ExecutionState asState() {
		return isValid() ? (command == null ? ExecutionState.NOT_EXISTENT : ExecutionState.READY) : ExecutionState.NO_COMMAND;
	}
	
	public ExecutionState execute() {
		return execute(manager);
	}
	
	public ExecutionState execute(CommandManager manager) {
		if(!arguments.isLocked()) {
			arguments.setLocked(true);
		}
		return manager.execute(this);
	}
	
	public CommandProcess lock() {
		if(!isLocked()) {
			locked = true;
		}
		return this;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	/*
	 * 
	 */
	
	private ObjectLockedException locked() {
		return new ObjectLockedException("Cannot edit a locked object!");
	}
	
}
