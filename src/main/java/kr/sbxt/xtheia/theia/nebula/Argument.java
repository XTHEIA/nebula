package kr.sbxt.xtheia.theia.nebula;

public interface Argument<TParsed>
{
	boolean parsable(String arg);
	TParsed parse(String value);
}
