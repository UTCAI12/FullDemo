package com.ilianazz.ai12poc.common.data;

import java.io.Serializable;

@FunctionalInterface
public interface UpdateBehavior<T extends Serializable> {

    void onUpdate(T value);
}
