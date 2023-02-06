package com.hrytsik.crmbot.commands.authorization;


import com.hrytsik.crmbot.ExecutionContext;

import java.io.IOException;

public interface Registration {
    void execute(ExecutionContext executionContext) throws IOException;
}
