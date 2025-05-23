package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common;

import org.apache.wicket.model.IModel;
import org.javatuples.Sextet;

public interface ISixChosenParameterState<
        TInitialState,
        TChosenParam1,
        TChosenParam2,
        TChosenParam3,
        TChosenParam4,
        TChosenParam5,
        TChosenParam6,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IChosenParameterState<
        TInitialState,
        Sextet<
            IModel<TChosenParam1>,
            IModel<TChosenParam2>,
            IModel<TChosenParam3>,
            IModel<TChosenParam4>,
            IModel<TChosenParam5>,
            IModel<TChosenParam6>>,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult> {}
