package com.example.semesterschedulingapp.model;

public class Programs {

    private int program_id;
    private String program_name;

    public Programs(int program_id, String program_name) {
        this.program_id = program_id;
        this.program_name = program_name;
    }

    public int getProgram_id() {
        return program_id;
    }

    public String getProgram_name() {
        return program_name;
    }
}
