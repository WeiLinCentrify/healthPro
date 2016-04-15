package com.example.yishafang.healthpro.Model;

/**
 * Created by yellowstar on 12/11/15.
 */
public enum ApptStatusEnum {
    ALL((byte)0), NEW((byte)1), BOOKED((byte)2), COMPLETED((byte) 3), CANCELLED((byte)4), EXPIRED((byte)5), MISSED((byte) 6);

    private final byte id;

    ApptStatusEnum(byte id) {
        this.id = id;
    }

    public byte id() {
        return this.id;
    }
}
