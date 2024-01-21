package com.apulbere.crop.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes a functionality that might change or disappear in the future without prior warning.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Experimental {
}
