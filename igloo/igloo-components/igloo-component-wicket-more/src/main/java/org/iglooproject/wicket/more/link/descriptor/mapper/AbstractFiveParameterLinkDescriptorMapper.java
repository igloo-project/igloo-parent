package org.iglooproject.wicket.more.link.descriptor.mapper;

import igloo.wicket.model.Models;
import org.apache.wicket.model.IModel;
import org.javatuples.Quartet;
import org.javatuples.Quintet;

public abstract class AbstractFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T5>
    implements IFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T5> {
  private static final long serialVersionUID = -6898001132755212341L;

  @Override
  public void detach() {}

  @Override
  public abstract L map(
      Quintet<
              ? extends IModel<T1>,
              ? extends IModel<T2>,
              ? extends IModel<T3>,
              ? extends IModel<T4>,
              ? extends IModel<T5>>
          param);

  @Override
  public final L map(
      IModel<T1> model1,
      IModel<T2> model2,
      IModel<T3> model3,
      IModel<T4> model4,
      IModel<T5> model5) {
    return map(Quintet.with(model1, model2, model3, model4, model5));
  }

  @Override
  public IFourParameterLinkDescriptorMapper<L, T2, T3, T4, T5> setParameter1(
      final IModel<T1> model1) {
    return new AbstractFourParameterLinkDescriptorMapper<L, T2, T3, T4, T5>() {
      private static final long serialVersionUID = 1L;

      @Override
      public L map(
          Quartet<
                  ? extends IModel<T2>,
                  ? extends IModel<T3>,
                  ? extends IModel<T4>,
                  ? extends IModel<T5>>
              param) {
        return AbstractFiveParameterLinkDescriptorMapper.this.map(param.addAt0(model1));
      }

      @Override
      public void detach() {
        super.detach();
        model1.detach();
        AbstractFiveParameterLinkDescriptorMapper.this.detach();
      }
    };
  }

  @Override
  public IFourParameterLinkDescriptorMapper<L, T2, T3, T4, T5> ignoreParameter1() {
    return setParameter1(Models.<T1>placeholder());
  }

  @Override
  public AbstractFourParameterLinkDescriptorMapper<L, T1, T3, T4, T5> setParameter2(
      final IModel<T2> model2) {
    return new AbstractFourParameterLinkDescriptorMapper<L, T1, T3, T4, T5>() {
      private static final long serialVersionUID = 1L;

      @Override
      public L map(
          Quartet<
                  ? extends IModel<T1>,
                  ? extends IModel<T3>,
                  ? extends IModel<T4>,
                  ? extends IModel<T5>>
              param) {
        return AbstractFiveParameterLinkDescriptorMapper.this.map(param.addAt1(model2));
      }

      @Override
      public void detach() {
        super.detach();
        model2.detach();
        AbstractFiveParameterLinkDescriptorMapper.this.detach();
      }
    };
  }

  @Override
  public AbstractFourParameterLinkDescriptorMapper<L, T1, T3, T4, T5> ignoreParameter2() {
    return setParameter2(Models.<T2>placeholder());
  }

  @Override
  public AbstractFourParameterLinkDescriptorMapper<L, T1, T2, T4, T5> setParameter3(
      final IModel<T3> model3) {
    return new AbstractFourParameterLinkDescriptorMapper<L, T1, T2, T4, T5>() {
      private static final long serialVersionUID = 1L;

      @Override
      public L map(
          Quartet<
                  ? extends IModel<T1>,
                  ? extends IModel<T2>,
                  ? extends IModel<T4>,
                  ? extends IModel<T5>>
              param) {
        return AbstractFiveParameterLinkDescriptorMapper.this.map(param.addAt2(model3));
      }

      @Override
      public void detach() {
        super.detach();
        model3.detach();
        AbstractFiveParameterLinkDescriptorMapper.this.detach();
      }
    };
  }

  @Override
  public AbstractFourParameterLinkDescriptorMapper<L, T1, T2, T4, T5> ignoreParameter3() {
    return setParameter3(Models.<T3>placeholder());
  }

  @Override
  public AbstractFourParameterLinkDescriptorMapper<L, T1, T2, T3, T5> setParameter4(
      final IModel<T4> model4) {
    return new AbstractFourParameterLinkDescriptorMapper<L, T1, T2, T3, T5>() {
      private static final long serialVersionUID = 1L;

      @Override
      public L map(
          Quartet<
                  ? extends IModel<T1>,
                  ? extends IModel<T2>,
                  ? extends IModel<T3>,
                  ? extends IModel<T5>>
              param) {
        return AbstractFiveParameterLinkDescriptorMapper.this.map(param.addAt3(model4));
      }

      @Override
      public void detach() {
        super.detach();
        model4.detach();
        AbstractFiveParameterLinkDescriptorMapper.this.detach();
      }
    };
  }

  @Override
  public AbstractFourParameterLinkDescriptorMapper<L, T1, T2, T3, T5> ignoreParameter4() {
    return setParameter4(Models.<T4>placeholder());
  }

  @Override
  public AbstractFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4> setParameter5(
      final IModel<T5> model5) {
    return new AbstractFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4>() {
      private static final long serialVersionUID = 1L;

      @Override
      public L map(
          Quartet<
                  ? extends IModel<T1>,
                  ? extends IModel<T2>,
                  ? extends IModel<T3>,
                  ? extends IModel<T4>>
              param) {
        return AbstractFiveParameterLinkDescriptorMapper.this.map(param.addAt4(model5));
      }

      @Override
      public void detach() {
        super.detach();
        model5.detach();
        AbstractFiveParameterLinkDescriptorMapper.this.detach();
      }
    };
  }

  @Override
  public AbstractFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4> ignoreParameter5() {
    return setParameter5(Models.<T5>placeholder());
  }
}
