package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common;

import org.apache.wicket.model.IModel;
import org.javatuples.Quintet;

public interface IFiveChosenParameterState<
        TInitialState,
        TChosenParam1,
        TChosenParam2,
        TChosenParam3,
        TChosenParam4,
        TChosenParam5,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IChosenParameterState<
        TInitialState,
        Quintet<
            IModel<TChosenParam1>,
            IModel<TChosenParam2>,
            IModel<TChosenParam3>,
            IModel<TChosenParam4>,
            IModel<TChosenParam5>>,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult> {}
