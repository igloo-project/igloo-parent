package org.iglooproject.wicket.more.markup.html.model;

import org.apache.wicket.util.lang.Bytes;

public enum BytesUnit {
  BYTE {
    @Override
    public double toDouble(Bytes bytesValue) {
      return bytesValue.bytes();
    }

    @Override
    public Bytes fromDouble(double doubleValue) {
      return Bytes.bytes(doubleValue);
    }

    @Override
    public Bytes fromLong(long longValue) {
      return Bytes.bytes(longValue);
    }
  },
  KILOBYTE {
    @Override
    public double toDouble(Bytes bytesValue) {
      return bytesValue.kilobytes();
    }

    @Override
    public Bytes fromDouble(double doubleValue) {
      return Bytes.kilobytes(doubleValue);
    }

    @Override
    public Bytes fromLong(long longValue) {
      return Bytes.kilobytes(longValue);
    }
  },
  MEGABYTE {
    @Override
    public double toDouble(Bytes bytesValue) {
      return bytesValue.megabytes();
    }

    @Override
    public Bytes fromDouble(double doubleValue) {
      return Bytes.megabytes(doubleValue);
    }

    @Override
    public Bytes fromLong(long longValue) {
      return Bytes.megabytes(longValue);
    }
  },
  GIGABYTE {
    @Override
    public double toDouble(Bytes bytesValue) {
      return bytesValue.gigabytes();
    }

    @Override
    public Bytes fromDouble(double doubleValue) {
      return Bytes.gigabytes(doubleValue);
    }

    @Override
    public Bytes fromLong(long longValue) {
      return Bytes.gigabytes(longValue);
    }
  },
  TERABYTE {
    @Override
    public double toDouble(Bytes bytesValue) {
      return bytesValue.terabytes();
    }

    @Override
    public Bytes fromDouble(double doubleValue) {
      return Bytes.gigabytes(doubleValue);
    }

    @Override
    public Bytes fromLong(long longValue) {
      return Bytes.gigabytes(longValue);
    }
  };

  public abstract double toDouble(Bytes bytesValue);

  public abstract Bytes fromDouble(double doubleValue);

  public abstract Bytes fromLong(long longValue);
}
