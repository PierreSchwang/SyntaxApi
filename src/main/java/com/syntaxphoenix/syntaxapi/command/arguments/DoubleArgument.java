package com.syntaxphoenix.syntaxapi.command.arguments;

import com.syntaxphoenix.syntaxapi.command.ArgumentSerializer;
import com.syntaxphoenix.syntaxapi.command.ArgumentType;
import com.syntaxphoenix.syntaxapi.command.BaseArgument;

/**
 * @author Lauriichen
 *
 */
public class DoubleArgument extends BaseArgument {
	
	private Double value;
	
	public DoubleArgument() {
		this.value = 0D;
	}
	
	public DoubleArgument(Double value) {
		this.value = value;
	}
	
	@Override
	public ArgumentType getType() {
		return ArgumentType.DOUBLE;
	}

	@Override
	public Object asObject() {
		return value;
	}
	
	public Double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return toString(ArgumentSerializer.DEFAULT);
	}

	@Override
	public String toString(ArgumentSerializer serializer) {
		return serializer.toString(this);
	}

}
