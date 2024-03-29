package com.moviesearcher.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviesearcher.R
import com.moviesearcher.common.PosterDialog
import com.moviesearcher.common.credentials.CredentialsHolder
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.common.utils.Constants.ERROR_MESSAGE
import com.moviesearcher.common.utils.Status
import com.moviesearcher.databinding.FragmentPersonInfoBinding
import com.moviesearcher.person.adapter.combinedcredits.CombinedCreditsAdapter
import com.moviesearcher.person.adapter.combinedcredits.images.PersonImagesAdapter
import com.moviesearcher.person.model.images.Profile
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

@AndroidEntryPoint
class PersonInfoFragment : Fragment() {

    private var _binding: FragmentPersonInfoBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<PersonInfoFragmentArgs>()

    private val viewModel by viewModels<PersonViewModel>()

    @Inject
    lateinit var credentialsHolder: CredentialsHolder
    private val sessionId: String
        get() = credentialsHolder.getSessionId()
    private val accountId: Long
        get() = credentialsHolder.getAccountId()

    private lateinit var filmographyRecyclerView: RecyclerView
    private lateinit var imagesRecyclerView: RecyclerView
    private lateinit var personPhotoImageView: ImageView
    private lateinit var personName: TextView
    private lateinit var personBorn: TextView
    private lateinit var personDied: TextView
    private lateinit var personBio: TextView
    private lateinit var personKnownFor: TextView
    private lateinit var buttonSeeAllImages: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var personInfoCardView: CardView
    private lateinit var filmographyCardView: CardView
    private lateinit var imagesCardView: CardView
    private lateinit var ageOfDeathTv: TextView
    private lateinit var personImagesAdapter: PersonImagesAdapter
    private lateinit var filmographyAdapter: CombinedCreditsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val personId = args.personId

        filmographyRecyclerView = binding.filmographyRecyclerView
        imagesRecyclerView = binding.imagesRecyclerView
        personPhotoImageView = binding.photoImageView
        personName = binding.personNameTextView
        personBorn = binding.bornTextView
        personDied = binding.diedTextView
        personBio = binding.bioTextView
        buttonSeeAllImages = binding.buttonSeeAllImages
        personKnownFor = binding.knownForTextView
        progressBar = binding.progressBarPerson
        personInfoCardView = binding.mainPersonInfoCardView
        filmographyCardView = binding.filmographyCardView
        imagesCardView = binding.imagesCardView
        ageOfDeathTv = binding.ageOfDeathTv

        setupAdapters()
        setupObservers()

        buttonSeeAllImages.setOnClickListener {
            val action =
                PersonInfoFragmentDirections.actionPersonInfoFragmentToImagesFragment()
            action.personId = personId

            findNavController().navigate(action)
        }
    }

    private fun setupAdapters() {
        personImagesAdapter = PersonImagesAdapter() {
            PosterDialog(it).show(childFragmentManager, "PosterDialog")
        }
        filmographyAdapter = CombinedCreditsAdapter(findNavController())

        imagesRecyclerView.adapter = personImagesAdapter
        imagesRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL,
            false
        )

        filmographyRecyclerView.adapter = filmographyAdapter
        filmographyRecyclerView.layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
    }

    private fun setupObservers() {
        viewModel.getPerson().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { personInfo ->
                        val dialog = PosterDialog(personInfo.profile_path.toString())
                        personPhotoImageView.loadImage(Constants.IMAGE_URL + personInfo.profile_path)
                        personName.text = personInfo.name
                        personBorn.text =
                            getString(R.string.born).format(personInfo.birthday).replace("-", ".")

                        if (personInfo.deathday == null) {
                            personDied.visibility = View.GONE
                        } else {
                            val born = LocalDate.parse(personInfo.birthday)
                            val died = LocalDate.parse(personInfo.deathday)

                            val period: Period = Period.between(born, died)

                            personDied.text =
                                getString(R.string.died).format(personInfo.deathday)
                                    .replace("-", ".")
                            ageOfDeathTv.text =
                                getString(R.string.aged).format(period.years.toString())
                        }

                        personBio.text = personInfo.biography
                        personKnownFor.text = personInfo.known_for_department

                        personBio.setOnClickListener {
                            MaterialAlertDialogBuilder(requireContext())
                                .setMessage(personInfo.biography)
                                .show()
                        }

                        personPhotoImageView.setOnClickListener {
                            dialog.show(childFragmentManager, "PosterDialogFragment")
                        }
                    }
                    progressBar.visibility = View.GONE
                    personInfoCardView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    personInfoCardView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        }

        viewModel.getPersonCombinedCredits().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { combinedCreditsItems ->
                        filmographyAdapter.submitList(combinedCreditsItems.cast)
                    }
                    progressBar.visibility = View.GONE
                    filmographyCardView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    filmographyCardView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        }

        viewModel.getPersonImages().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { imagesItems ->
                        if (!imagesItems.profiles.isNullOrEmpty()) {
                            var tenImages = imagesItems.profiles

                            while (tenImages?.size!! > 10) {
                                tenImages = tenImages.dropLast(1) as MutableList<Profile>
                            }

                            imagesItems.apply {
                                profiles = tenImages
                            }

                            personImagesAdapter.submitList(imagesItems.profiles)

                            imagesCardView.visibility = View.VISIBLE
                        }

                        progressBar.visibility = View.GONE
                    }
                }
                Status.LOADING -> {
                    imagesCardView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}