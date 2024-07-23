package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common;

import org.apache.wicket.model.IModel;
import org.javatuples.Pair;

public interface ITwoChosenParameterState<
        TInitialState,
        TChosenParam1,
        TChosenParam2,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IChosenParameterState<
        TInitialState,
        Pair<IModel<TChosenParam1>, IModel<TChosenParam2>>,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult> {}
