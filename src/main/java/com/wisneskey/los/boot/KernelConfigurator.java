package com.wisneskey.los.boot;

import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.Service;

public interface KernelConfigurator {

	void setRunMode(RunMode runMode);
	
	<T> void register(Service<T> service);
}
