package fr.thedarven.scenarios.helper;

public interface StorablePreset<T> {

    T getPresetValue();

    void setPresetValue(T value);

}
