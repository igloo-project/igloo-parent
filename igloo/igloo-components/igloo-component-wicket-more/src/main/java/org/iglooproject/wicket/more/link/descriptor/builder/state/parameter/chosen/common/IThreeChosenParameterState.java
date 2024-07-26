package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common;

import org.apache.wicket.model.IModel;
import org.javatuples.Triplet;

public interface IThreeChosenParameterState<
        TInitialState,
        TChosenParam1,
        TChosenParam2,
        TChosenParam3,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IChosenParameterState<
        TInitialState,
        Triplet<IModel<TChosenParam1>, IModel<TChosenParam2>, IModel<TChosenParam3>>,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult> {}
