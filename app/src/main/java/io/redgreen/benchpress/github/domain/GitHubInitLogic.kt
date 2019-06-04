package io.redgreen.benchpress.github.domain

import com.spotify.mobius.First
import com.spotify.mobius.First.first
import com.spotify.mobius.Init

object GitHubInitLogic : Init<GitHubModel, GitHubEffect> {
    override fun init(model: GitHubModel): First<GitHubModel, GitHubEffect> {
        return first(GitHubModel.LOADING, setOf(FetchSquareReposEffect))
    }
}