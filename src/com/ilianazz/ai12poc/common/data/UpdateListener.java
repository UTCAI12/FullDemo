package com.ilianazz.ai12poc.common.data;

import java.io.Serializable;

@FunctionalInterface
public interface UpdateListener<T extends Serializable> {

    void onUpdate(T value);
}
