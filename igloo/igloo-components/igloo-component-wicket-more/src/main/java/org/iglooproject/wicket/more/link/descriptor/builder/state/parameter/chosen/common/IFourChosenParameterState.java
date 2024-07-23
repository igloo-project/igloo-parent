package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common;

import org.apache.wicket.model.IModel;
import org.javatuples.Quartet;

public interface IFourChosenParameterState<
        TInitialState,
        TChosenParam1,
        TChosenParam2,
        TChosenParam3,
        TChosenParam4,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IChosenParameterState<
        TInitialState,
        Quartet<
            IModel<TChosenParam1>,
            IModel<TChosenParam2>,
            IModel<TChosenParam3>,
            IModel<TChosenParam4>>,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult> {}
