package kr.sbxt.xtheia.theia.nebula;

public interface NebulaItemModifier
{
	String getName();
	Argument<?>[] getArguments();
	boolean apply(String[] args);
	
}
