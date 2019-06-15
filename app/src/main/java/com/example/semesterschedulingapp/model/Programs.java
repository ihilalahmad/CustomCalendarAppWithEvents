package com.example.semesterschedulingapp.model;

public class Programs {

    private int program_id;
    private int batch_id;
    private String program_name;
    private String batch_name;

    public Programs(int program_id, int batch_id, String program_name, String batch_name) {
        this.program_id = program_id;
        this.batch_id = batch_id;
        this.program_name = program_name;
        this.batch_name = batch_name;
    }

    public int getProgram_id() {
        return program_id;
    }

    public int getBatch_id() {
        return batch_id;
    }

    public String getProgram_name() {
        return program_name;
    }

    public String getBatch_name() {
        return batch_name;
    }
}
