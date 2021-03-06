package ru.skillbranch.skillarticles.ui.dialogs

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_categories.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.ui.base.BaseDialogFragment
import ru.skillbranch.skillarticles.ui.base.Binding
import ru.skillbranch.skillarticles.ui.delegates.RenderProp
import ru.skillbranch.skillarticles.viewmodels.articles.ArticlesState
import ru.skillbranch.skillarticles.viewmodels.articles.ArticlesViewModel
import ru.skillbranch.skillarticles.viewmodels.base.IViewModelState

class ChoseCategoryDialog : BaseDialogFragment<ArticlesViewModel>() {
    override val viewModel: ArticlesViewModel by activityViewModels()
    override val layout = R.layout.fragment_categories
    private val args: ChoseCategoryDialogArgs by navArgs()
    override val binding by lazy { CategoriesBinding() }


    //TODO save checked state and implement custom items
    private val categories by lazy { args.categories }
    private val checked by lazy { args.selectedCategories }

    private val categoriesAdapter = CategoryAdapter { category, isChecked ->
        val categories = binding.changedCategories
        binding.changedCategories = (if (isChecked) categories.plus(category.categoryId)
            else categories.minus(category.categoryId)).distinct()
    }

    override val buildDialog: AlertDialog.Builder.() -> AlertDialog.Builder = {
        this.setTitle("Chose category")
            .setPositiveButton("Apply") { _, _ ->
                viewModel.applyCategories(binding.changedCategories)
            }.setNegativeButton("Reset") { _, _ ->
                viewModel.applyCategories(emptyList())
            }
    }

    override fun setupViews() {
        with(categories_list) {
            layoutManager = LinearLayoutManager(context)
            adapter = categoriesAdapter
        }

        if (binding.selectedCategories.isEmpty()) {
            binding.selectedCategories = checked.toList()
            binding.changedCategories = checked.toList()
        }
    }

    inner class CategoriesBinding : Binding() {

        var changedCategories: List<String> = emptyList()

        var selectedCategories: List<String> by RenderProp(emptyList()) { selected ->
            val checkedCategories = categories.map { it to selected.contains(it.categoryId) }
            categoriesAdapter.submitList(checkedCategories)
        }

        override fun bind(data: IViewModelState) {
            data as ArticlesState
        }

        override fun saveUi(outState: Bundle) {
            outState.putStringArray(::changedCategories.name, changedCategories.toTypedArray())
        }

        override fun restoreUi(savedState: Bundle?) {
            val categories = savedState?.getStringArray(::changedCategories.name)?.toList() ?: emptyList()
            changedCategories = categories
            selectedCategories = categories
        }
    }
}