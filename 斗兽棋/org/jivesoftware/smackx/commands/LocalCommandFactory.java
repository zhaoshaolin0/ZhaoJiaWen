package org.jivesoftware.smackx.commands;

public abstract interface LocalCommandFactory
{
  public abstract LocalCommand getInstance()
    throws InstantiationException, IllegalAccessException;
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.commands.LocalCommandFactory
 * JD-Core Version:    0.7.0.1
 */