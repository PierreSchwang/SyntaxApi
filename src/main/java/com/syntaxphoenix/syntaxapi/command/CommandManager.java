package com.syntaxphoenix.syntaxapi.command;

import java.util.Map.Entry;

import com.syntaxphoenix.syntaxapi.logging.SynLogger;
import com.syntaxphoenix.syntaxapi.utils.alias.Alias;
import com.syntaxphoenix.syntaxapi.utils.alias.AliasMap;
import com.syntaxphoenix.syntaxapi.utils.java.Arrays;

/**
 * @author Lauriichen
 *
 */
public class CommandManager {
	
	private final AliasMap<BaseCommand> commands = new AliasMap<>();
	private ArgumentValidator validator = ArgumentValidator.DEFAULT;
	private SynLogger logger = null;
	
	private String splitter = " ";
	private String prefix = "!";
	
	/*
	 * 
	 */
	
	public AliasMap<BaseCommand> getClonedMap() {
		return commands.clone();
	}
	
	@SuppressWarnings("unchecked")
	public Entry<Alias, BaseCommand>[] getEntries()  {
		return commands.entrySet().toArray(new Entry[0]); 
	}
	
	public BaseCommand[] getCommands() {
		return commands.values().toArray(new BaseCommand[0]);
	}
	
	public Alias[] getAliases() {
		return commands.keySet().toArray(new Alias[0]);
	}
	
	/*
	 * 
	 */
	
	public String getPrefix() {
		return prefix;
	}
	
	public CommandManager setPrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}
	
	public String getSplitter() {
		return splitter;
	}
	
	public CommandManager setSplitter(String splitter) {
		this.splitter = splitter;
		return this;
	}
	
	public ArgumentValidator getValidator() {
		return validator;
	}
	
	public CommandManager setValidator(ArgumentValidator validator) {
		this.validator = validator;
		return this;
	}
	
	public CommandManager setLogger(SynLogger logger) {
		this.logger = logger;
		return this;
	}
	
	public boolean hasLogger() {
		return logger != null;
	}
	
	public SynLogger getLogger() {
		return logger;
	}
	
	/*
	 * 
	 */
	
	public CommandManager register(BaseCommand command, String name, String... aliases) {
		return register(command, new Alias(name, aliases));
	}
	
	public CommandManager register(BaseCommand command, String description, String name, String[] aliases) {
		return register(command, new Alias(name, aliases).setDescription(description));
	}
	
	public CommandManager register(BaseCommand command, Alias alias) {
		if(commands.hasConflict(alias).isEmpty()) {
			commands.put(alias, command);
		}
		return this;
	}
	
	public CommandProcess process(String message) {
		CommandProcess process = new CommandProcess(this);
		if(!message.startsWith(prefix) || message.equals(prefix)) {
			return process.lock();
		}
		return process(message.replace(prefix, "").split(splitter));
	}
	
	public CommandProcess process(String... message) {
		return process(new CommandProcess(this), message);
	}
	
	private CommandProcess process(CommandProcess process, String... message) {
		String command = message[0].toLowerCase();
		process.setLabel(command);
		if(command.isEmpty()) {
			return process.lock();
		}
		process.setValid(true);
		if(!commands.containsKey(command)) {
			return process.lock();
		}
		process.setCommand(commands.get(command));
		process.setArguments(new Arguments(validator.process(Arrays.subArray(message, 1))));
		return process.lock();
	}
	
	/*
	 * 
	 */
	
	public ExecutionState execute(String message) {
		return process(message).execute();
	}
	
	public ExecutionState execute(CommandProcess process) {
		ExecutionState state = process.asState();
		if(!state.isRunnable()) {
			return state;
		}
		return execute(process.getCommand(), process.constructInfo(), process.getArguments());
	}
	
	public ExecutionState execute(BaseCommand command, BaseInfo info, Arguments arguments) {
		try {
			command.execute(info, arguments);
		} catch(Throwable throwable) {
			if(hasLogger()) {
				logger.log(throwable);
			}
			return ExecutionState.FAILED;
		}
		return ExecutionState.SUCCESS;
	}
	
}
